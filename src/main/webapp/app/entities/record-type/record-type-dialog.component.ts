import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {RecordType} from "./record-type.model";
import {RecordTypePopupService} from "./record-type-popup.service";
import {RecordTypeService} from "./record-type.service";
@Component({
	selector: 'jhi-record-type-dialog',
	templateUrl: './record-type-dialog.component.html'
})
export class RecordTypeDialogComponent implements OnInit {

	recordType: RecordType;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private recordTypeService: RecordTypeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['recordType']);
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
		if (this.recordType.id !== undefined) {
			this.recordTypeService.update(this.recordType)
				.subscribe((res: RecordType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.recordTypeService.create(this.recordType)
				.subscribe((res: RecordType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: RecordType) {
		this.eventManager.broadcast({name: 'recordTypeListModification', content: 'OK'});
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
	selector: 'jhi-record-type-popup',
	template: ''
})
export class RecordTypePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private recordTypePopupService: RecordTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.recordTypePopupService
					.open(RecordTypeDialogComponent, params['id']);
			} else {
				this.modalRef = this.recordTypePopupService
					.open(RecordTypeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
