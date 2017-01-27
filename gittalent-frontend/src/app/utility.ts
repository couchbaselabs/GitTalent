import {Injectable, Inject} from "@angular/core";
import {Http, Request, RequestMethod, Headers, RequestOptions} from "@angular/http";
import {Observable, Subject} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import { environment } from '../environments/environment';

@Injectable()
export class Utility {

    host: string = environment.gittalentBackendURL;

    constructor(private http: Http) { }

    makePostRequestWithObservable(endpoint: string, params: Array<string>, body: Object): Observable<any> {
        let fullUrl: string = this.host + endpoint;
        if(params && params.length > 0) {
            fullUrl = fullUrl + "/" + params.join("/");
        }
        console.log("HTTP POST REQUEST: " + fullUrl, body);
        var requestHeaders = new Headers();
        requestHeaders.append("Content-Type", "application/json");
        return this.http.request(new Request({
            method: RequestMethod.Post,
            url: fullUrl,
            body: JSON.stringify(body),
            headers: requestHeaders
        }))
        .map(result => result.json())
        .do(result => console.log("HTTP POST RESPONSE [" + fullUrl + "]: ", result));
    }

    makePostRequest(endpoint: string, params: Array<string>, body: Object): Promise<any> {
        var fullUrl: string = this.host + endpoint;
        if(params && params.length > 0) {
            fullUrl = fullUrl + "/" + params.join("/");
        }
        console.log("HTTP POST REQUEST: " + fullUrl, body);
        return new Promise((resolve, reject) => {
            var requestHeaders = new Headers();
            requestHeaders.append("Content-Type", "application/json");
            this.http.request(new Request({
                method: RequestMethod.Post,
                url: fullUrl,
                body: JSON.stringify(body),
                headers: requestHeaders
            }))
            .map(result => result.json())
            .subscribe(result => {
                console.log("HTTP POST RESPONSE [" + fullUrl + "]: ", result);
                resolve(result);
            }, (error) => {
                reject(error);
            });
        });
    }

    makePutRequest(endpoint: string, params: Array<string>, body: Object): Promise<any> {
        var fullUrl: string = this.host + endpoint;
        if(params && params.length > 0) {
            fullUrl = fullUrl + "/" + params.join("/");
        }
        console.log("HTTP PUT REQUEST: " + fullUrl, body);
        return new Promise((resolve, reject) => {
            var requestHeaders = new Headers();
            requestHeaders.append("Content-Type", "application/json");
            this.http.request(new Request({
                method: RequestMethod.Put,
                url: fullUrl,
                body: JSON.stringify(body),
                headers: requestHeaders
            }))
            .map(result => result.json())
            .subscribe(result => {
                console.log("HTTP PUT RESPONSE [" + fullUrl + "]: ", result);
                resolve(result);
            }, (error) => {
                reject(error);
            });
        });
    }

    makeDeleteRequest(endpoint: string, params: Array<string>): Promise<any> {
        var fullUrl: string = this.host + endpoint;
        if(params && params.length > 0) {
            fullUrl = fullUrl + "/" + params.join("/");
        }
        console.log("HTTP DELETE REQUEST: " + fullUrl);
        return new Promise((resolve, reject) => {
            this.http.request(new Request({
                method: RequestMethod.Delete,
                url: fullUrl
            }))
            .subscribe(result => {
                console.log("HTTP DELETE RESPONSE [" + fullUrl + "]: ", result);
                resolve(result);
            }, (error) => {
                reject(error);
            });
        });
    }

    makeGetRequest(endpoint: string, params: Array<string>, query?: any, headers?: Headers): Promise<any> {
        var fullUrl: string = this.host + endpoint;
        if(params && params.length > 0) {
            fullUrl = fullUrl + "/" + params.join("/");
        }
        if(query) {
            fullUrl = fullUrl + "?" + this.serializeQueryParams(query);
        }
        console.log("HTTP GET REQUEST: ", fullUrl);
        let options = new RequestOptions({ headers: headers });
        return new Promise((resolve, reject) => {
            this.http.get(fullUrl, options)
            .map(result => result.json())
            .subscribe(result => {
                console.log("HTTP GET RESPONSE [" + fullUrl + "]: ", result);
                resolve(result);
            }, (error) => {
                reject(error);
            });
        });
    }

    serializeQueryParams(params: any) {
        let query = [];
        for(let p in params) {
            if (params.hasOwnProperty(p)) {
                query.push(encodeURIComponent(p) + "=" + encodeURIComponent(params[p]));
            }
        }
        return query.join("&");
    }

}
