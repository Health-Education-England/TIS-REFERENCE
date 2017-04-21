import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {FundingType} from "./funding-type.model";
import {FundingTypeService} from "./funding-type.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-funding-type',
	templateUrl: './funding-type.component.html'
})
export class FundingTypeComponent implements OnInit, OnDestroy {
	fundingTypes: FundingType[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private fundingTypeService: FundingTypeService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['fundingType']);
	}

	loadAll() {
		this.fundingTypeService.query().subscribe(
			(res: Response) => {
				this.fundingTypes = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInFundingTypes();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: FundingType) {
		return item.id;
	}


	registerChangeInFundingTypes() {
		this.eventSubscriber = this.eventManager.subscribe('fundingTypeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
