import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {TariffRate} from "./tariff-rate.model";
import {TariffRateService} from "./tariff-rate.service";

@Component({
	selector: 'jhi-tariff-rate-detail',
	templateUrl: './tariff-rate-detail.component.html'
})
export class TariffRateDetailComponent implements OnInit, OnDestroy {

	tariffRate: TariffRate;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private tariffRateService: TariffRateService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['tariffRate']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.tariffRateService.find(id).subscribe(tariffRate => {
			this.tariffRate = tariffRate;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
