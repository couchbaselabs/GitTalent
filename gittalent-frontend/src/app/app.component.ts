import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
    styleUrls: ['app.component.css']
})
export class AppComponent {

    public query: string;

    public constructor(private router: Router) {
        this.query = "";
    }

    public search() {
        this.router.navigate(["search", this.query]);
        this.query = "";
    }

}
