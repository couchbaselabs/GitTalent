import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'phone'
})
export class PhonePipe implements PipeTransform {

    public transform(value: string, args: any) {
        let result = value;
        if(value) {
            let cleaned = value.replace(/[^\d\+]/g, "");
            if(cleaned.length == 10) {
                result = cleaned.substr(0, 3) + "-" + cleaned.substr(3, 3) + "-" + cleaned.substr(6, 4);
            }
        }
        return result;
    }

}
