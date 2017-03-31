import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Grade} from "./grade.model";
import {GradePopupService} from "./grade-popup.service";
import {GradeService} from "./grade.service";
@Component({
	selector: 'jhi-grade-dialog',
	templateUrl: './grade-dialog.component.html'
})
export class GradeDialogComponent implements OnInit {

	grade: Grade;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private gradeService: GradeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['grade']);
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
		if (this.grade.id !== undefined) {
			this.gradeService.update(this.grade)
				.subscribe((res: Grade) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.gradeService.create(this.grade)
				.subscribe((res: Grade) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Grade) {
		this.eventManager.broadcast({name: 'gradeListModification', content: 'OK'});
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
	selector: 'jhi-grade-popup',
	template: ''
})
export class GradePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gradePopupService: GradePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.gradePopupService
					.open(GradeDialogComponent, params['id']);
			} else {
				this.modalRef = this.gradePopupService
					.open(GradeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
