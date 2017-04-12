import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {TrainingNumberType} from "./training-number-type.model";
import {TrainingNumberTypeService} from "./training-number-type.service";

@Component({
	selector: 'jhi-training-number-type-detail',
	templateUrl: './training-number-type-detail.component.html'
})
export class TrainingNumberTypeDetailComponent implements OnInit, OnDestroy {

	trainingNumberType: TrainingNumberType;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trainingNumberTypeService: TrainingNumberTypeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['trainingNumberType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.trainingNumberTypeService.find(id).subscribe(trainingNumberType => {
			this.trainingNumberType = trainingNumberType;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
