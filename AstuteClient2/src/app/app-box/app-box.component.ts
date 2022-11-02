import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-app-box',
  templateUrl: './app-box.component.html',
  styleUrls: ['./app-box.component.css']
})
export class AppBoxComponent implements OnInit {
  @Input() name;
  @Input() symbol;
  @Input() description;
  @Input() color;

  constructor() { }

  ngOnInit() {
    this.color = this.getColor();
  }

  getColor() {
    if (this.color) {
      return this.color;
    } else {
      const clr = this.padStart(Math.floor((Math.random() * 16777215)).toString(16), 6, '0');
      console.log (clr);
      return '#' + clr;
    }
  }

  padStart(str: string, l: number, append: string) {
    let len = str.length;
    while (len < l) {
      str = append + str;
      len = str.length;
    }
    return str;
  }
}
