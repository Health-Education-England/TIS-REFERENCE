import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Status} from "./status.model";
import {StatusPopupService} from "./status-popup.service";
import {StatusService} from "./status.service";
@Component({
	selector: 'jhi-status-dialog',
	templateUrl: './status-dialog.component.html'
})
export class StatusDialogComponent implements OnInit {

	status: Status;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private statusService: StatusService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['status']);
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
		if (this.status.id !== undefined) {
			this.statusService.update(this.status)
				.subscribe((res: Status) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.statusService.create(this.status)
				.subscribe((res: Status) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Status) {
		this.eventManager.broadcast({name: 'statusListModification', content: 'OK'});
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
	selector: 'jhi-status-popup',
	template: ''
})
export class StatusPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private statusPopupService: StatusPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.statusPopupService
					.open(StatusDialogComponent, params['id']);
			} else {
				this.modalRef = this.statusPopupService
					.open(StatusDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
