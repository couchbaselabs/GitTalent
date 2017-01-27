import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {IHit, ITerm, IDDNumericFacet, IDDTermFacet} from '../../entityInterfaces';
import { Utility } from '../../utility';
import {Observable, Subject} from 'rxjs/Rx';

@Component({
    selector: 'search',
    templateUrl: './search.html'
})
export class SearchComponent implements OnInit {

    public hits: Array<IHit>;
    public terms: Array<ITerm>;
    public facets: Array<any>;
    public drillDownTerms: Array<any>;
    public drillDownFacets: Array<any>;
    private activeFacet: any;
    private totalPages: number;
    public isRequesting: boolean;
    public isRequestingForInfScroll: boolean;
    public dataSubscription: Array<any>;
    public dataPool: any;
    public searchData: any;

    public constructor(private route: ActivatedRoute, private utility: Utility) {
        this.hits = [];
        this.facets = [];
        this.drillDownFacets = [];
        this.drillDownTerms = [];
        this.terms = [];
        this.totalPages = -1;
        this.activeFacet = {
            "page": 0,
            "pageSize": 50
        };
        this.dataSubscription = [];
        this.dataPool = new Subject();
    }

    public ngOnInit() {
        this.route.params.subscribe(params => {
            this.activeFacet.term = params["query"];
            this.hits = [];
            if(this.activeFacet.types) {
                delete this.activeFacet.types;
            }
            this.activeFacet.page = 0;
            this.search(this.setIsRequesting, false, true);
        });
    }

    public getJsonData(hit: any) {
        return JSON.stringify(hit, null, 2);
    }

    public populateResults(resultHits: any) {
        for(let i = 0; i < resultHits.length; i++) {
            let hit = <IHit>{};
            hit.id = resultHits[i].id;
            hit.type = resultHits[i].fields.type;
            hit.fragments = [];
            if(resultHits[i].fragments) {
                if(hit.type == "ticket") {
                  hit.fragments = resultHits[i].fragments.title;
                  hit.assignedId = resultHits[i].fields.assignedId;
                } else if(hit.type == "developer" || hit.type == "organization") {
                    if (resultHits[i].fields["developerInfo.username"]) {hit.fragments.push(resultHits[i].fields["developerInfo.username"]);}
                    if (resultHits[i].fields["developerInfo.firstName"]) {hit.fragments.push(resultHits[i].fields["developerInfo.firstName"]);}
                    if (resultHits[i].fields["developerInfo.lastName"]) {hit.fragments.push(resultHits[i].fields["developerInfo.lastName"]);}
                    if (resultHits[i].fields["developerInfo.email"]) {hit.fragments.push(resultHits[i].fields["developerInfo.email"]);}
                    if (resultHits[i].fragments["repositories.mainLanguage"]) {hit.fragments.push(resultHits[i].fragments["repositories.mainLanguage"]);}
                    if (resultHits[i].fragments["repositories.repoName"]) {hit.fragments.push(resultHits[i].fragments["repositories.repoName"]);}
                    if (resultHits[i].fragments["repositories.fullName"]) {hit.fragments.push(resultHits[i].fragments["repositories.fullName"]);}
                }
            }
            hit.score = resultHits[i].score;
            this.hits.push(hit);
        }
    }

    public search(toggleSpinner: any, withTypes: boolean = false, refreshFacets: boolean = false) {
        toggleSpinner(true);
        this.dataSubscription.push(
            this.utility.makePostRequestWithObservable("/fulltext/", [], this.activeFacet)
                .finally(() => { toggleSpinner(false); })
                .subscribe(result => {
                    this.totalPages = Math.ceil(result.total_hits / this.activeFacet.pageSize);
                    if(refreshFacets) {
                        this.facets = [];
                        this.terms = [];
                        this.terms = result.facets.type.terms;
                        for(let i = 0; i < this.terms.length; i++) {
                          this.facets.push({ checked: withTypes, term: this.terms[i].term});
                        }
                    }
                    this.populateResults(result.hits);
                }, error => {
                    console.error(error);
                }
            )
        );
    }


  public drillDownSearch(toggleSpinner: any, checkedField: string, checked:boolean = false, refreshFacets: boolean = false, ) {
    toggleSpinner(true);
    let endpoint = "/fulltext/";
    if (this.activeFacet.types ) {endpoint += this.activeFacet.types[0];}
    this.dataSubscription.push(
      this.utility.makePostRequestWithObservable(endpoint, [], this.activeFacet)
        .finally(() => { toggleSpinner(false); })
        .subscribe(result => {
            this.totalPages = Math.ceil(result.total_hits / this.activeFacet.pageSize);
            if(refreshFacets) {
              this.drillDownTerms = [];
              var newDrillDownFacets = [];
              for (var key in result.facets) {
                if (result.facets[key].numeric_ranges) {
                  var nfacet = <IDDNumericFacet>{};
                  nfacet.name = key;
                  nfacet.total = result.facets[key].total;
                  nfacet.field = result.facets[key].field;
                  nfacet.missing = result.facets[key].missing;
                  nfacet.other = result.facets[key].other;
                  nfacet.facets = result.facets[key].numeric_ranges;
                  nfacet.type = "numeric";
                  this.drillDownTerms.push(nfacet);
                  var ddf = [];
                  for(let i = 0; i < nfacet.facets.length; i++) {
                    if (nfacet.field == checkedField) {
                      ddf.push({checked: checked, term: nfacet.facets[i].name});
                    } else {
                      var found = false;
                      for (let j = 0; j < this.drillDownFacets.length; j++) {
                        for (let k = 0; k < this.drillDownFacets[j].length; k++) {
                          if (this.drillDownFacets[j][k].term == nfacet.facets[i].name) {
                            ddf.push({checked: this.drillDownFacets[j][k].checked, term: nfacet.facets[i].name});
                            found = true;
                          }
                        }
                      }
                      if (!found) {
                        ddf.push({checked: false, term: nfacet.facets[i].name});
                      }
                    }
                  }
                  newDrillDownFacets.push(ddf);
                }
                if (result.facets[key].terms) {
                  var tfacet = <IDDTermFacet>{};
                  tfacet.name = key;
                  tfacet.total = result.facets[key].total;
                  tfacet.field = result.facets[key].field;
                  tfacet.missing = result.facets[key].missing;
                  tfacet.other = result.facets[key].other;
                  tfacet.facets = result.facets[key].terms;
                  tfacet.type = "term";
                  this.drillDownTerms.push(tfacet);
                  var ddf = [];
                  for(let i = 0; i < tfacet.facets.length; i++) {
                    if (tfacet.field == checkedField) {
                      ddf.push({checked: checked, term: tfacet.facets[i].term});
                    } else {
                      var found = false;
                      for (let j = 0; j < this.drillDownFacets.length; j++) {
                        for (let k = 0; k < this.drillDownFacets[j].length; k++) {
                          console.log(this.drillDownFacets[j][k]);
                          if (this.drillDownFacets[j][k].term == tfacet.facets[i].term) {
                            ddf.push({checked: this.drillDownFacets[j][k].checked, term: tfacet.facets[i].term});
                            console.log(ddf);
                            found = true;
                          }
                        }
                      }
                      if (!found) {
                        ddf.push({checked: false, term: tfacet.facets[i].term});
                        console.log(ddf);
                      }
                    }
                  }
                  newDrillDownFacets.push(ddf);
                }
              }
              this.drillDownFacets = newDrillDownFacets;
            }
            this.populateResults(result.hits);
          }, error => {
            console.error(error);
          }
        )
    );
  }


  public ddfacetSearch(checked: boolean, name: string, field:string, min?:number, max?:number, page: number = 0) {
    this.hits = [];
    this.activeFacet.page = page;
    for(let i = 0; i < this.dataSubscription.length; i++) {
      this.dataSubscription[i].unsubscribe();
    }
    this.dataSubscription = [];
    if(checked) {
      if (field == "createdAt") {
        this.activeFacet["dateLowerBound"] = min;
        this.activeFacet["dateHigherBound"] = max;
      }
      this.activeFacet[field] = name;
    }
    else {
      if (field == "createdAt") {
        delete this.activeFacet["dateLowerBound"];
        delete this.activeFacet["dateHigherBound"];
      }
      delete this.activeFacet[field];
    }
    this.drillDownSearch(this.setIsRequesting, field, checked,  true);
  }


  public facetSearch(checked: boolean, name: string, page: number = 0) {
        this.hits = [];
        this.activeFacet.page = page;
        for(let i = 0; i < this.dataSubscription.length; i++) {
            this.dataSubscription[i].unsubscribe();
        }
        this.dataSubscription = [];
        if(checked) {
            this.activeFacet.types = [name];
            // select only newly activated term
            var newTerms = [];
            for(let i = 0; i < this.terms.length; i++) {
              if (this.terms[i].term == this.activeFacet.types) {
                newTerms.push(this.terms[i]);
              }
            }
            this.terms = newTerms;
            this.drillDownSearch(this.setIsRequesting, name, checked, true);
        } else {
            delete this.activeFacet.types;
            this.drillDownFacets = [];
            this.drillDownTerms = [];
            this.search(this.setIsRequesting, false, true);
        }
    }

    public onScrollDown() {
        if(this.activeFacet.page < this.totalPages - 1) {
            this.activeFacet.page++;
          if (this.drillDownFacets[0]) {
            this.drillDownSearch(this.setIsRequestingInfiniteScroll, this.activeFacet.types[0],false, false);
          } else {
            this.search(this.setIsRequestingInfiniteScroll, this.activeFacet.types ? true : false, false);
          }
        }
    }

    public setIsRequesting = (status: boolean) => {
      this.isRequesting = status;
    }

    public setIsRequestingInfiniteScroll = (status: boolean) => {
      this.isRequestingForInfScroll = status;
    }

}
