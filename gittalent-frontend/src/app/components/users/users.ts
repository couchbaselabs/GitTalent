import { Component, OnInit } from '@angular/core';
import { IUser } from '../../entityInterfaces';
import { PhonePipe } from '../../pipes/phonenumber';
import { Utility } from "../../utility";

@Component({
    selector: 'users',
    templateUrl: './users.html'
})

export class UsersComponent implements OnInit {

    public users : Array<IUser>;
    public user : IUser;

    public constructor(private utility: Utility) {
        this.users = [];
        this.user = <IUser>{};
    }

    public ngOnInit() {
        this.getAll();
    }

    public save() {
        if(this.user.firstName && this.user.lastName) {
            if(this.user.id) {
                this.utility.makePutRequest("/user", [this.user.id], this.user).then(result => {
                    for(let i = 0; i < this.users.length; i++) {
                        if(this.users[i].id == result.id) {
                            this.users[i] = result;
                        }
                    }
                }, error => {
                    console.error(error);
                });
            } else {
                this.user.id = String(Math.floor((Math.random() * 10000) + 1));
                this.utility.makePostRequest("/user", [], this.user).then(result => {
                    this.users.push(result);
                }, error => {
                    console.error(error);
                });
            }
            this.user = <IUser>{};
        }
    }

    public getAll() {
        this.utility.makeGetRequest("/user", []).then(result => {
            for(let i = 0; i < result._embedded.user.length; i++) {
                this.users.push(result._embedded.user[i]);
            }
        }, error => {
            console.error(error);
        });
    }

    public getById(id: string) {
        this.utility.makeGetRequest("/user", [id]).then(result => {
            this.user = result;
        }, error => {
            console.error(error);
        });
    }

    public delete(index: number) {
        this.utility.makeDeleteRequest("/user", [this.users[index].id]).then(result => {
            this.users.splice(index, 1);
        }, error => {
            console.error(error);
        });
    }

}
