import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  currentUser: any = {};

  constructor() {
  }

  ngOnInit() {
    this.currentUser = this.getSessionUser();
    console.log(this.currentUser);
  }

  private getSessionUser() {
    return localStorage.getItem('SESSION_USER');
  }
}
