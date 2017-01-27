import { Component, OnInit } from '@angular/core';
import { IOrganization, IAddress, ILatLon } from '../../entityInterfaces';
import { Utility } from "../../utility";

declare var bootbox: any;

@Component({
  selector: 'organizations',
  templateUrl: './organizations.html'
})

export class OrganizationsComponent implements OnInit {

    public organizations : Array<IOrganization>;
    public organization : IOrganization;
    public pages : any;
    public isRequesting: boolean;

    public constructor(private utility: Utility) {
        this.organization = <IOrganization>{ address: <IAddress>{ gis: <ILatLon>{}}};
        this.organizations = [];
        this.pages = {
            "size": 15,
            "number": 0
        }
    }

    public ngOnInit() {
        this.loadOrganizations(0);
    }

    public getById(id: string) {
        this.utility.makeGetRequest("/organization", [id]).then(result => {
            this.organization = result;
        }, error => {
            console.error(error);
        });
    }

    public delete(index: number) {
        bootbox.confirm("Click OK if you sure you want to delete organization " + this.organizations[index].id + ".", (confirmed) => {
            if(!confirmed)
                return;
            this.utility.makeDeleteRequest("/organization", [this.organizations[index].id]).then(result => {
                this.organizations.splice(index, 1);
            }, error => {
                console.error(error);
            });
        });
    }

    public save() {
        // TODO: angular validation?

        // if this is a new organization, update it in the grid
        if(this.organization.id) {
            this.utility.makePutRequest("/organization", [this.organization.id], this.organization).then(result => {
                for(let i = 0; i < this.organizations.length; i++) {
                    if(this.organizations[i].id == result.id) {
                        this.organizations[i] = result;
                    }
                }
            }, error => {
                console.error(error);
            });
        // otherwise this is a new organization, so assign it an ID and add it to the grid
        } else {
            this.organization.id = String(Math.floor((Math.random() * 10000) + 1));
            this.utility.makePostRequest("/organization", [], this.organization).then(result => {
                this.organizations.push(result);
            }, error => {
                console.error(error);
            });
        }


        // clear out the form
        this.organization = <IOrganization>{ address: <IAddress>{ gis: <ILatLon>{}}};
    }

    public getAll(page: number) {
        let query = {
            "size": this.pages.size,
            "page": page
        };
        this.isRequesting = true;
        this.utility.makeGetRequest("/organization", [], query).then(result => {
            this.organizations = [];
            this.pages = result.page;
            for(let i = 0; i < result._embedded.organization.length; i++) {
              this.organizations.push(result._embedded.organization[i]);
            }
          this.isRequesting = false;
        }, error => {
          this.isRequesting = false;
            console.error(error);
        });
    }


    private loadOrganizations(page: number) {
        this.isRequesting = true;
        let query = {
            "size": this.pages.size,
            "page": page
        }
        this.utility.makeGetRequest("/organization", [], query).then(result => {
            this.organizations = [];
            this.pages = result.page;
            for(let i = 0; i < result._embedded.organization.length; i++) {
                this.organizations.push(result._embedded.organization[i]);
            }
            this.isRequesting = false;
        }, error => {
            this.isRequesting = false;
            console.error(error);
        });
    }
}
