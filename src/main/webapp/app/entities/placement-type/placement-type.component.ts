import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {PlacementType} from "./placement-type.model";
import {PlacementTypeService} from "./placement-type.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-placement-type',
	templateUrl: './placement-type.component.html'
})
export class PlacementTypeComponent implements OnInit, OnDestroy {
	placementTypes: PlacementType[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private placementTypeService: PlacementTypeService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['placementType']);
	}

	loadAll() {
		this.placementTypeService.query().subscribe(
			(res: Response) => {
				this.placementTypes = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInPlacementTypes();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: PlacementType) {
		return item.id;
	}


	registerChangeInPlacementTypes() {
		this.eventSubscriber = this.eventManager.subscribe('placementTypeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
