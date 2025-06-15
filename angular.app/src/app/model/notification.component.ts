import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';  // importe aqui
import { NotificationService, Notification } from '../services/notification.service';

@Component({
  selector: 'app-notification',
  standalone: true,           // Adicione standalone: true
  imports: [CommonModule],     // Importe CommonModule para usar ngClass e ngIf
  template: `
    <div class="notification-container" *ngIf="notification">
      <div class="notification" [ngClass]="notification.type">
        {{ notification.message }}
        <span class="progress-bar"></span>
      </div>
    </div>
  `,
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  notification: Notification | null = null;

  constructor(private notificationService: NotificationService) {}

  ngOnInit() {
    this.notificationService.notification$.subscribe(n => {
      this.notification = n;
    });
  }
}
