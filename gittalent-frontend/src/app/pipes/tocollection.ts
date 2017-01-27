import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'tocollection'
})
export class ToCollectionPipe implements PipeTransform {

    public transform(value: number, args: any) {
        //Array(5).fill(5)
        return Array.apply(null, {length: value}).map(Number.call, Number)
    }

}
