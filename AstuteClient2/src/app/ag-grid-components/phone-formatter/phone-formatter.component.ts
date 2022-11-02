import { Component } from '@angular/core';

@Component({
  selector: 'app-phone-formatter-cell',
  template: `
    <span>({{firstThree}}) {{secondThree}}-{{lastFour}}</span>
  `
})
export class PhoneFormatterComponent  {
    firstThree: string;
    secondThree: string;
    lastFour: string;

    agInit(params: any): void {
      const whole: string = params.value;
      this.firstThree = whole.substr(0, 3);
      this.secondThree = whole.substr(3, 3);
      this.lastFour = whole.substr(6, 4);
    }
}
