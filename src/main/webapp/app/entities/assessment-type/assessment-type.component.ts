import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {AssessmentType} from "./assessment-type.model";
import {AssessmentTypeService} from "./assessment-type.service";
import {ITEMS_PER_PAGE, Principal} from "../../shared";

@Component({
	selector: 'jhi-assessment-type',
	templateUrl: './assessment-type.component.html'
})
export class AssessmentTypeComponent implements OnInit, OnDestroy {
	assessmentTypes: AssessmentType[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private assessmentTypeService: AssessmentTypeService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['assessmentType']);
	}

	loadAll() {
		this.assessmentTypeService.query().subscribe(
			(res: Response) => {
				this.assessmentTypes = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInAssessmentTypes();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: AssessmentType) {
		return item.id;
	}


	registerChangeInAssessmentTypes() {
		this.eventSubscriber = this.eventManager.subscribe('assessmentTypeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
