import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { Recommendation } from '../../models/recommendation.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recommendation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './recommendation.page.html'
})
export class RecommendationPage {

  private route = inject(ActivatedRoute);
  private api = inject(ApiService);

  data?: Recommendation;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.api.getRecommendationAI(id)
      .subscribe(res => this.data = res);
  }
}
