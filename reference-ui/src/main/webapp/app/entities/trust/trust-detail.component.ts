import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Trust} from "./trust.model";
import {TrustService} from "./trust.service";

@Component({
	selector: 'jhi-trust-detail',
	templateUrl: './trust-detail.component.html'
})
export class TrustDetailComponent implements OnInit, OnDestroy {

	trust: Trust;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trustService: TrustService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['trust']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.trustService.find(id).subscribe(trust => {
			this.trust = trust;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
