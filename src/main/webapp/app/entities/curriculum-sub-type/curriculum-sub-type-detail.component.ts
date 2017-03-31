import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {CurriculumSubType} from "./curriculum-sub-type.model";
import {CurriculumSubTypeService} from "./curriculum-sub-type.service";

@Component({
	selector: 'jhi-curriculum-sub-type-detail',
	templateUrl: './curriculum-sub-type-detail.component.html'
})
export class CurriculumSubTypeDetailComponent implements OnInit, OnDestroy {

	curriculumSubType: CurriculumSubType;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private curriculumSubTypeService: CurriculumSubTypeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['curriculumSubType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.curriculumSubTypeService.find(id).subscribe(curriculumSubType => {
			this.curriculumSubType = curriculumSubType;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
