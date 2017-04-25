import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Gender} from "./gender.model";
import {GenderService} from "./gender.service";

@Component({
	selector: 'jhi-gender-detail',
	templateUrl: './gender-detail.component.html'
})
export class GenderDetailComponent implements OnInit, OnDestroy {

	gender: Gender;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private genderService: GenderService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['gender']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.genderService.find(id).subscribe(gender => {
			this.gender = gender;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
