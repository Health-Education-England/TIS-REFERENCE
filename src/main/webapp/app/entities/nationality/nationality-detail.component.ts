import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Nationality} from "./nationality.model";
import {NationalityService} from "./nationality.service";

@Component({
	selector: 'jhi-nationality-detail',
	templateUrl: './nationality-detail.component.html'
})
export class NationalityDetailComponent implements OnInit, OnDestroy {

	nationality: Nationality;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private nationalityService: NationalityService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['nationality']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.nationalityService.find(id).subscribe(nationality => {
			this.nationality = nationality;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
