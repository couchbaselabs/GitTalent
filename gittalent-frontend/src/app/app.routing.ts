import { Routes } from "@angular/router";
import { OrganizationsComponent } from "./components/organizations/organizations";
import { DevelopersComponent } from "./components/developers/developers";
import { DeveloperComponent } from "./components/developer/developer";
import { SearchComponent } from "./components/search/search";
import { UsersComponent } from "./components/users/users";
import { AnalyticsComponent } from "./components/analytics/analytics";
import { ClusterStatusComponent } from "./components/clusterstatus/clusterstatus";

export const appRoutes: Routes = [
    { 'path' : 'users', 'component' : UsersComponent },
    { 'path' : 'organizations', 'component' : OrganizationsComponent },
    { 'path' : '', 'component' : DevelopersComponent },
    { 'path' : 'developer/:id', 'component' : DeveloperComponent },
    { 'path' : 'search/:query', 'component' : SearchComponent },
    { 'path' : 'analytics', 'component' : AnalyticsComponent },
    { 'path' : 'clusterstatus', 'component' : ClusterStatusComponent }
];

export const appComponents: any = [
    OrganizationsComponent,
    DevelopersComponent,
    DeveloperComponent,
    SearchComponent,
    UsersComponent,
    AnalyticsComponent,
    ClusterStatusComponent
];