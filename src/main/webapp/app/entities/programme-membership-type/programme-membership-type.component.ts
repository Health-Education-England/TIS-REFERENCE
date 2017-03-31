import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {ProgrammeMembershipType} from "./programme-membership-type.model";
import {ProgrammeMembershipTypeService} from "./programme-membership-type.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-programme-membership-type',
	templateUrl: './programme-membership-type.component.html'
})
export class ProgrammeMembershipTypeComponent implements OnInit, OnDestroy {
	programmeMembershipTypes: ProgrammeMembershipType[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private programmeMembershipTypeService: ProgrammeMembershipTypeService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['programmeMembershipType']);
	}

	loadAll() {
		this.programmeMembershipTypeService.query().subscribe(
			(res: Response) => {
				this.programmeMembershipTypes = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInProgrammeMembershipTypes();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: ProgrammeMembershipType) {
		return item.id;
	}


	registerChangeInProgrammeMembershipTypes() {
		this.eventSubscriber = this.eventManager.subscribe('programmeMembershipTypeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
