import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {AssessmentType} from "./assessment-type.model";
import {AssessmentTypeService} from "./assessment-type.service";

@Component({
	selector: 'jhi-assessment-type-detail',
	templateUrl: './assessment-type-detail.component.html'
})
export class AssessmentTypeDetailComponent implements OnInit, OnDestroy {

	assessmentType: AssessmentType;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private assessmentTypeService: AssessmentTypeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['assessmentType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.assessmentTypeService.find(id).subscribe(assessmentType => {
			this.assessmentType = assessmentType;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
