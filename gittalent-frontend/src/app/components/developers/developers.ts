import { Component, OnInit } from '@angular/core';
import { IDeveloper, IOrganization, IAddress, IDeveloperInfo } from '../../entityInterfaces';
import { Utility } from '../../utility';

@Component({
    selector: 'developers',
    templateUrl: './developers.html'
})
export class DevelopersComponent implements OnInit {

    public developers : Array<IDeveloper>;
    public developer : IDeveloper;
    public search : any;
    public pages : any;
    public isRequesting: boolean;

    public constructor(private utility: Utility) {
        this.developers = [];
        this.developer = <IDeveloper>{developerInfo: <IDeveloperInfo>{}, address: <IAddress>{}};
        this.search = {firstname: "", lastname: ""};
        this.pages = {
            "size": 15,
            "number": 0
        };
    }

    public ngOnInit() {
        this.getAll(0);
    }

    public getAll(page: number) {
        let query = {
            "size": this.pages.size,
            "page": page
        };
        this.isRequesting = true;
        this.utility.makeGetRequest("/developer", [], query).then(result => {
            this.developers = [];
            this.pages = result.page;
            for(let i = 0; i < result._embedded.developer.length; i++) {
              this.developers.push(result._embedded.developer[i]);
            }
          this.isRequesting = false;
        }, error => {
          this.isRequesting = false;
            console.error(error);
        });
    }

    public save() {
        this.developer.id = String(Math.floor((Math.random() * 10000) + 1));
        this.utility.makePostRequest("/developer", [], this.developer).then(result => {
            this.developers.push(result);
            this.developer = <IDeveloper>{developerInfo: <IDeveloperInfo>{}, address: <IAddress>{}};
        }, error => {
            console.error(error);
        });
    }

    public fetch() {
        this.isRequesting = true;
        this.utility.makeGetRequest("/developer/search", [], this.search).then(result => {
            this.developers = result.results;
            this.pages.totalElements = result.results.length;
            this.pages.totalPages = 0;
            this.isRequesting = false;
        }, error => {
            this.isRequesting = false;
            console.error(error);
        });
    }

    public clear() {
        this.developers = [];
        this.search = {firstname: "", lastname: ""};
        this.getAll(0);
    }

}
