import { Component, OnInit } from '@angular/core';
import { Utility } from "../../utility";

@Component({
    selector: 'clusterstatus',
    templateUrl: './clusterstatus.html',
})
export class ClusterStatusComponent implements OnInit {

    public nodes: Array<any>;
    public isRequesting: boolean;

    public constructor(private utility: Utility) {
        this.nodes = [];
    }

    public ngOnInit() {
        this.refreshStatus();
    }

    public cleanHostName(hostname: string) {
        return hostname
            .replace("http://","")
            .replace(":8091","")
            .replace(".compute.amazonaws.com","");
    }

    // tag::refreshStatus[]
    public refreshStatus() {
        this.isRequesting = true;
        this.utility.makeGetRequest("/pools/nodes", []).then(result => {
            this.nodes = result.nodes;
            this.isRequesting = false;
        }, error => {
            this.isRequesting = false;
            
            // if there's an error, assume the whole cluster is down, and
            // just mark all the nodes as server_down

            for(let i=0;i<this.nodes.length;i++) {
                this.nodes[i].status = "server_down";
            }
            console.error(error);
        });
    }
    // end::refreshStatus[]
}
