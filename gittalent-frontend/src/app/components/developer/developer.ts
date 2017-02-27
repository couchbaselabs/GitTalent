import { Component, OnInit } from '@angular/core';
import { Location } from "@angular/common";
import { ActivatedRoute, Router } from '@angular/router';
import { IDeveloper, IDeveloperInfo, ITicket, IOrganization, IAddress } from '../../entityInterfaces';
import { Utility } from "../../utility";
import { PhonePipe } from '../../pipes/phonenumber';
import { UnknownPipe } from '../../pipes/unknown';
import {Headers} from "@angular/http";

declare var bootbox: any;

@Component({
    selector: 'developers',
    templateUrl: './developer.html'
})

export class DeveloperComponent implements OnInit {

    public developer: IDeveloper;
    public contacts: Array<IDeveloper>;
    public isEditing: boolean;
    public isRequesting: boolean;
    public ticket: ITicket;
    public devSchema: any;

    public constructor(private route: ActivatedRoute, private location: Location, private utility: Utility) {
        this.developer = <IDeveloper>{history: [], contacts: [], address: <IAddress>{}, developerInfo: <IDeveloperInfo>{}};
        this.developer.developerInfo.socialMedia = {};
        this.contacts = [];
        this.ticket = <ITicket>{};
        this.isEditing = false;
        this.devSchema = {};
    }

    public ngOnInit() {
        this.route.params.subscribe(params => {
            this.developer.id = params["id"];
            this.getById();
            this.getDevSchema();
        });
    }

    public getById() {
        this.utility.makeGetRequest("/developer/fullcontact", [this.developer.id]).then(result => {
            this.developer = <IDeveloper> result.results[0];

            if(!this.developer.history) {
                this.developer.history = [];
            }

            if(!this.developer.contacts) {
                this.developer.contacts = [];
            }
            if(!this.developer.developerInfo.socialMedia) {
                this.developer.developerInfo.socialMedia = {};
            }
        }, error => {
            console.error(error);
        });
    }

    public getDevSchema() {
      var requestHeaders = new Headers();
      requestHeaders.append("Accept", "application/schema+json");
      this.utility.makeGetRequest("/profile/developer", [], null,requestHeaders).then(result => {
        this.devSchema = result;
      }, error => {
        console.error(error);
      });
    }




    public toggleEditing() {
        this.isEditing = !this.isEditing;
    }

    public save() {
        this.utility.makePutRequest("/developer/info", [this.developer.id], this.developer).then(result => {
            this.isEditing = false;
        }, error => {
            console.error(error);
        });
    }

    public addContact(email: string) {
        if(email) {
            this.getByEmail(email).then(result => {
                this.developer.contacts.push(result);
                this.utility.makePutRequest("/developer/addContact", [this.developer.id], {contactId: result.id}).then(result => {
                    console.log(result);
                }, error => {
                    console.error(error);
                });
            }, error => {
                console.error(error);
            });
        }
    }

    public deleteContact(developerId, contactId) {
        this.utility.makePutRequest("/developer/removeContact", [developerId], {contactId: contactId}).then(result => {
            for(let i = 0; i < this.developer.contacts.length; i++) {
                if(this.developer.contacts[i].id == contactId) {
                    this.developer.contacts.splice(i, 1);
                }
            }
            //console.log(result);
        }, error => {
            console.error(error);
        });
    }

    public addTicket() {
        // create an ID
        this.ticket.id = "ticket::" + String(Math.floor((Math.random() * 10000) + 1));

        // convert date into a number
        this.ticket.createdAt = (new Date(this.ticket.createdAt)).getTime();

        this.developer.history.push(this.ticket);

        // create new ticket, and get that ticket id
        this.createTicket(this.ticket).then(result => {
            // use the ticket ID to add the ticket to the contact
            this.utility.makeGetRequest("/developer/addTicket", [this.developer.id, this.ticket.id]).then(result => {
                console.log(result);
            }, error => {
                console.error(error);
            });
            console.log("here");
            this.ticket = <ITicket>{};
        });
    }

    public prepareCreateTicket() {
        this.ticket = <ITicket>{};
    }

    public formatDue(timestamp : number) {
        var date = new Date(timestamp);
        return timestamp ? (date.getMonth()+1) + "/" + (date.getDate()+1) + "/" + date.getFullYear() : "";
    }

    public createTicket(ticket: ITicket) {
        return new Promise((resolve, reject) => {
            this.utility.makePostRequest("/ticket", [], ticket).then(result => {
                resolve(result);
            }, error => {
                reject(error);
            });
        });
    }

    public getByEmail(email: string): Promise<IDeveloper> {
        return new Promise((resolve, reject) => {
            this.utility.makeGetRequest("/developer/getByEmail", [], {email: email}).then(result => {
                resolve(result.results[0]);
            }, error => {
                reject(error);
            });
        });
    }

    public delete() {
        bootbox.confirm("Click OK if you sure you want to delete developer " + this.developer.id + ".", (confirmed) => {
            if(!confirmed)
                return;
            this.utility.makeDeleteRequest("/developer", [this.developer.id]).then(result => {
                this.location.back();
            }, error => {
                console.error(error);
            });
        });
    }

    public populateTicketDetails(ticketId) {
        return new Promise((resolve, reject) => {
            this.utility.makeGetRequest("/ticket", [ticketId]).then(result => {
                console.log(result.results);
                this.ticket = result;
            }, error => {
                reject(error);
            });
        });
    }
}
