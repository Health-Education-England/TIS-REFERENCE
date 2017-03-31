import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {RecordType} from "./record-type.model";
import {RecordTypeService} from "./record-type.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-record-type',
	templateUrl: './record-type.component.html'
})
export class RecordTypeComponent implements OnInit, OnDestroy {
	recordTypes: RecordType[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private recordTypeService: RecordTypeService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['recordType']);
	}

	loadAll() {
		this.recordTypeService.query().subscribe(
			(res: Response) => {
				this.recordTypes = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInRecordTypes();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: RecordType) {
		return item.id;
	}


	registerChangeInRecordTypes() {
		this.eventSubscriber = this.eventManager.subscribe('recordTypeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
