import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {Gender} from "./gender.model";
import {GenderService} from "./gender.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-gender',
	templateUrl: './gender.component.html'
})
export class GenderComponent implements OnInit, OnDestroy {
	genders: Gender[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private genderService: GenderService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['gender']);
	}

	loadAll() {
		this.genderService.query().subscribe(
			(res: Response) => {
				this.genders = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInGenders();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: Gender) {
		return item.id;
	}


	registerChangeInGenders() {
		this.eventSubscriber = this.eventManager.subscribe('genderListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
