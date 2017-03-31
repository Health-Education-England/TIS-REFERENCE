import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {AssessmentType} from "./assessment-type.model";
import {AssessmentTypePopupService} from "./assessment-type-popup.service";
import {AssessmentTypeService} from "./assessment-type.service";
@Component({
	selector: 'jhi-assessment-type-dialog',
	templateUrl: './assessment-type-dialog.component.html'
})
export class AssessmentTypeDialogComponent implements OnInit {

	assessmentType: AssessmentType;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private assessmentTypeService: AssessmentTypeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['assessmentType']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.assessmentType.id !== undefined) {
			this.assessmentTypeService.update(this.assessmentType)
				.subscribe((res: AssessmentType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.assessmentTypeService.create(this.assessmentType)
				.subscribe((res: AssessmentType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: AssessmentType) {
		this.eventManager.broadcast({name: 'assessmentTypeListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-assessment-type-popup',
	template: ''
})
export class AssessmentTypePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private assessmentTypePopupService: AssessmentTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.assessmentTypePopupService
					.open(AssessmentTypeDialogComponent, params['id']);
			} else {
				this.modalRef = this.assessmentTypePopupService
					.open(AssessmentTypeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
