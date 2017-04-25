import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {EthnicOrigin} from "./ethnic-origin.model";
import {EthnicOriginService} from "./ethnic-origin.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-ethnic-origin',
	templateUrl: './ethnic-origin.component.html'
})
export class EthnicOriginComponent implements OnInit, OnDestroy {
	ethnicOrigins: EthnicOrigin[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private ethnicOriginService: EthnicOriginService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['ethnicOrigin']);
	}

	loadAll() {
		this.ethnicOriginService.query().subscribe(
			(res: Response) => {
				this.ethnicOrigins = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInEthnicOrigins();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: EthnicOrigin) {
		return item.id;
	}


	registerChangeInEthnicOrigins() {
		this.eventSubscriber = this.eventManager.subscribe('ethnicOriginListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
