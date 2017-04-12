import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {DBC} from "./dbc.model";
import {DBCService} from "./dbc.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-dbc',
	templateUrl: './dbc.component.html'
})
export class DBCComponent implements OnInit, OnDestroy {
	dBCS: DBC[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private dBCService: DBCService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['dBC']);
	}

	loadAll() {
		this.dBCService.query().subscribe(
			(res: Response) => {
				this.dBCS = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInDBCS();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: DBC) {
		return item.id;
	}


	registerChangeInDBCS() {
		this.eventSubscriber = this.eventManager.subscribe('dBCListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
