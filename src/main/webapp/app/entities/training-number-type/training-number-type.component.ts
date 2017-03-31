import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {TrainingNumberType} from "./training-number-type.model";
import {TrainingNumberTypeService} from "./training-number-type.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-training-number-type',
	templateUrl: './training-number-type.component.html'
})
export class TrainingNumberTypeComponent implements OnInit, OnDestroy {
	trainingNumberTypes: TrainingNumberType[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trainingNumberTypeService: TrainingNumberTypeService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['trainingNumberType']);
	}

	loadAll() {
		this.trainingNumberTypeService.query().subscribe(
			(res: Response) => {
				this.trainingNumberTypes = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInTrainingNumberTypes();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: TrainingNumberType) {
		return item.id;
	}


	registerChangeInTrainingNumberTypes() {
		this.eventSubscriber = this.eventManager.subscribe('trainingNumberTypeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
