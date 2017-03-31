import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {GdcStatus} from "./gdc-status.model";
import {GdcStatusService} from "./gdc-status.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-gdc-status',
	templateUrl: './gdc-status.component.html'
})
export class GdcStatusComponent implements OnInit, OnDestroy {
	gdcStatuses: GdcStatus[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gdcStatusService: GdcStatusService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['gdcStatus']);
	}

	loadAll() {
		this.gdcStatusService.query().subscribe(
			(res: Response) => {
				this.gdcStatuses = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInGdcStatuses();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: GdcStatus) {
		return item.id;
	}


	registerChangeInGdcStatuses() {
		this.eventSubscriber = this.eventManager.subscribe('gdcStatusListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
