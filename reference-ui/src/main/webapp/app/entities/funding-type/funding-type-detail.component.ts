import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {FundingType} from "./funding-type.model";
import {FundingTypeService} from "./funding-type.service";

@Component({
	selector: 'jhi-funding-type-detail',
	templateUrl: './funding-type-detail.component.html'
})
export class FundingTypeDetailComponent implements OnInit, OnDestroy {

	fundingType: FundingType;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private fundingTypeService: FundingTypeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['fundingType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.fundingTypeService.find(id).subscribe(fundingType => {
			this.fundingType = fundingType;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
