import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {CurriculumSubType} from "./curriculum-sub-type.model";
import {CurriculumSubTypePopupService} from "./curriculum-sub-type-popup.service";
import {CurriculumSubTypeService} from "./curriculum-sub-type.service";
@Component({
	selector: 'jhi-curriculum-sub-type-dialog',
	templateUrl: './curriculum-sub-type-dialog.component.html'
})
export class CurriculumSubTypeDialogComponent implements OnInit {

	curriculumSubType: CurriculumSubType;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private curriculumSubTypeService: CurriculumSubTypeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['curriculumSubType']);
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
		if (this.curriculumSubType.id !== undefined) {
			this.curriculumSubTypeService.update(this.curriculumSubType)
				.subscribe((res: CurriculumSubType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.curriculumSubTypeService.create(this.curriculumSubType)
				.subscribe((res: CurriculumSubType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: CurriculumSubType) {
		this.eventManager.broadcast({name: 'curriculumSubTypeListModification', content: 'OK'});
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
	selector: 'jhi-curriculum-sub-type-popup',
	template: ''
})
export class CurriculumSubTypePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private curriculumSubTypePopupService: CurriculumSubTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.curriculumSubTypePopupService
					.open(CurriculumSubTypeDialogComponent, params['id']);
			} else {
				this.modalRef = this.curriculumSubTypePopupService
					.open(CurriculumSubTypeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
