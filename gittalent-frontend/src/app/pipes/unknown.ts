import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'unknown'
})
export class UnknownPipe implements PipeTransform {

    public transform(value: string, args: any) {
        return value && value.length > 0 ? value : "Unknown";
    }

}
