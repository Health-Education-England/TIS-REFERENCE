import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {TrainingNumberType} from "./training-number-type.model";
import {TrainingNumberTypePopupService} from "./training-number-type-popup.service";
import {TrainingNumberTypeService} from "./training-number-type.service";
@Component({
	selector: 'jhi-training-number-type-dialog',
	templateUrl: './training-number-type-dialog.component.html'
})
export class TrainingNumberTypeDialogComponent implements OnInit {

	trainingNumberType: TrainingNumberType;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private trainingNumberTypeService: TrainingNumberTypeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['trainingNumberType']);
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
		if (this.trainingNumberType.id !== undefined) {
			this.trainingNumberTypeService.update(this.trainingNumberType)
				.subscribe((res: TrainingNumberType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.trainingNumberTypeService.create(this.trainingNumberType)
				.subscribe((res: TrainingNumberType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: TrainingNumberType) {
		this.eventManager.broadcast({name: 'trainingNumberTypeListModification', content: 'OK'});
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
	selector: 'jhi-training-number-type-popup',
	template: ''
})
export class TrainingNumberTypePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private trainingNumberTypePopupService: TrainingNumberTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.trainingNumberTypePopupService
					.open(TrainingNumberTypeDialogComponent, params['id']);
			} else {
				this.modalRef = this.trainingNumberTypePopupService
					.open(TrainingNumberTypeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
