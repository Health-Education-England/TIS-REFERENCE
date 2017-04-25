import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {GmcStatus} from "./gmc-status.model";
import {GmcStatusPopupService} from "./gmc-status-popup.service";
import {GmcStatusService} from "./gmc-status.service";
@Component({
	selector: 'jhi-gmc-status-dialog',
	templateUrl: './gmc-status-dialog.component.html'
})
export class GmcStatusDialogComponent implements OnInit {

	gmcStatus: GmcStatus;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private gmcStatusService: GmcStatusService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gmcStatus']);
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
		if (this.gmcStatus.id !== undefined) {
			this.gmcStatusService.update(this.gmcStatus)
				.subscribe((res: GmcStatus) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.gmcStatusService.create(this.gmcStatus)
				.subscribe((res: GmcStatus) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: GmcStatus) {
		this.eventManager.broadcast({name: 'gmcStatusListModification', content: 'OK'});
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
	selector: 'jhi-gmc-status-popup',
	template: ''
})
export class GmcStatusPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gmcStatusPopupService: GmcStatusPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.gmcStatusPopupService
					.open(GmcStatusDialogComponent, params['id']);
			} else {
				this.modalRef = this.gmcStatusPopupService
					.open(GmcStatusDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
