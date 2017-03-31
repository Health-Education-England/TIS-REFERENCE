import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Country} from "./country.model";
import {CountryService} from "./country.service";

@Component({
	selector: 'jhi-country-detail',
	templateUrl: './country-detail.component.html'
})
export class CountryDetailComponent implements OnInit, OnDestroy {

	country: Country;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private countryService: CountryService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['country']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.countryService.find(id).subscribe(country => {
			this.country = country;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
