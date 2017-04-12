import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {SexualOrientation} from "./sexual-orientation.model";
import {SexualOrientationService} from "./sexual-orientation.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-sexual-orientation',
	templateUrl: './sexual-orientation.component.html'
})
export class SexualOrientationComponent implements OnInit, OnDestroy {
	sexualOrientations: SexualOrientation[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private sexualOrientationService: SexualOrientationService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['sexualOrientation']);
	}

	loadAll() {
		this.sexualOrientationService.query().subscribe(
			(res: Response) => {
				this.sexualOrientations = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInSexualOrientations();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: SexualOrientation) {
		return item.id;
	}


	registerChangeInSexualOrientations() {
		this.eventSubscriber = this.eventManager.subscribe('sexualOrientationListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
