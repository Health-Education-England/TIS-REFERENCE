import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {GdcStatus} from "./gdc-status.model";
import {GdcStatusPopupService} from "./gdc-status-popup.service";
import {GdcStatusService} from "./gdc-status.service";
@Component({
	selector: 'jhi-gdc-status-dialog',
	templateUrl: './gdc-status-dialog.component.html'
})
export class GdcStatusDialogComponent implements OnInit {

	gdcStatus: GdcStatus;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private gdcStatusService: GdcStatusService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gdcStatus']);
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
		if (this.gdcStatus.id !== undefined) {
			this.gdcStatusService.update(this.gdcStatus)
				.subscribe((res: GdcStatus) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.gdcStatusService.create(this.gdcStatus)
				.subscribe((res: GdcStatus) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: GdcStatus) {
		this.eventManager.broadcast({name: 'gdcStatusListModification', content: 'OK'});
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
	selector: 'jhi-gdc-status-popup',
	template: ''
})
export class GdcStatusPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gdcStatusPopupService: GdcStatusPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.gdcStatusPopupService
					.open(GdcStatusDialogComponent, params['id']);
			} else {
				this.modalRef = this.gdcStatusPopupService
					.open(GdcStatusDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
