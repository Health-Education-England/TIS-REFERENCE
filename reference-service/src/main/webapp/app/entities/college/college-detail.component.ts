import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {College} from "./college.model";
import {CollegeService} from "./college.service";

@Component({
	selector: 'jhi-college-detail',
	templateUrl: './college-detail.component.html'
})
export class CollegeDetailComponent implements OnInit, OnDestroy {

	college: College;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private collegeService: CollegeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['college']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.collegeService.find(id).subscribe(college => {
			this.college = college;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
