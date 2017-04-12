import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {ReligiousBelief} from "./religious-belief.model";
import {ReligiousBeliefService} from "./religious-belief.service";

@Component({
	selector: 'jhi-religious-belief-detail',
	templateUrl: './religious-belief-detail.component.html'
})
export class ReligiousBeliefDetailComponent implements OnInit, OnDestroy {

	religiousBelief: ReligiousBelief;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private religiousBeliefService: ReligiousBeliefService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['religiousBelief']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.religiousBeliefService.find(id).subscribe(religiousBelief => {
			this.religiousBelief = religiousBelief;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
