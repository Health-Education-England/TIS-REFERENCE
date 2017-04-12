import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {CurriculumSubType} from "./curriculum-sub-type.model";
import {CurriculumSubTypeService} from "./curriculum-sub-type.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-curriculum-sub-type',
	templateUrl: './curriculum-sub-type.component.html'
})
export class CurriculumSubTypeComponent implements OnInit, OnDestroy {
	curriculumSubTypes: CurriculumSubType[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private curriculumSubTypeService: CurriculumSubTypeService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['curriculumSubType']);
	}

	loadAll() {
		this.curriculumSubTypeService.query().subscribe(
			(res: Response) => {
				this.curriculumSubTypes = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInCurriculumSubTypes();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: CurriculumSubType) {
		return item.id;
	}


	registerChangeInCurriculumSubTypes() {
		this.eventSubscriber = this.eventManager.subscribe('curriculumSubTypeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
