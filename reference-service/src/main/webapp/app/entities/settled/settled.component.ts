import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {Settled} from "./settled.model";
import {SettledService} from "./settled.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-settled',
	templateUrl: './settled.component.html'
})
export class SettledComponent implements OnInit, OnDestroy {
	settleds: Settled[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private settledService: SettledService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['settled']);
	}

	loadAll() {
		this.settledService.query().subscribe(
			(res: Response) => {
				this.settleds = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInSettleds();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: Settled) {
		return item.id;
	}


	registerChangeInSettleds() {
		this.eventSubscriber = this.eventManager.subscribe('settledListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
