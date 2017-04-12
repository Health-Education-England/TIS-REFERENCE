import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {MaritalStatus} from "./marital-status.model";
import {MaritalStatusService} from "./marital-status.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-marital-status',
	templateUrl: './marital-status.component.html'
})
export class MaritalStatusComponent implements OnInit, OnDestroy {
	maritalStatuses: MaritalStatus[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private maritalStatusService: MaritalStatusService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['maritalStatus']);
	}

	loadAll() {
		this.maritalStatusService.query().subscribe(
			(res: Response) => {
				this.maritalStatuses = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInMaritalStatuses();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: MaritalStatus) {
		return item.id;
	}


	registerChangeInMaritalStatuses() {
		this.eventSubscriber = this.eventManager.subscribe('maritalStatusListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
