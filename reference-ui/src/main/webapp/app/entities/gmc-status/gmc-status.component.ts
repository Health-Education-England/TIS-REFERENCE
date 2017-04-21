import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {GmcStatus} from "./gmc-status.model";
import {GmcStatusService} from "./gmc-status.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-gmc-status',
	templateUrl: './gmc-status.component.html'
})
export class GmcStatusComponent implements OnInit, OnDestroy {
	gmcStatuses: GmcStatus[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gmcStatusService: GmcStatusService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['gmcStatus']);
	}

	loadAll() {
		this.gmcStatusService.query().subscribe(
			(res: Response) => {
				this.gmcStatuses = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInGmcStatuses();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: GmcStatus) {
		return item.id;
	}


	registerChangeInGmcStatuses() {
		this.eventSubscriber = this.eventManager.subscribe('gmcStatusListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
