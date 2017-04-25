import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {LeavingDestination} from "./leaving-destination.model";
import {LeavingDestinationPopupService} from "./leaving-destination-popup.service";
import {LeavingDestinationService} from "./leaving-destination.service";
@Component({
	selector: 'jhi-leaving-destination-dialog',
	templateUrl: './leaving-destination-dialog.component.html'
})
export class LeavingDestinationDialogComponent implements OnInit {

	leavingDestination: LeavingDestination;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private leavingDestinationService: LeavingDestinationService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['leavingDestination']);
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
		if (this.leavingDestination.id !== undefined) {
			this.leavingDestinationService.update(this.leavingDestination)
				.subscribe((res: LeavingDestination) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.leavingDestinationService.create(this.leavingDestination)
				.subscribe((res: LeavingDestination) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: LeavingDestination) {
		this.eventManager.broadcast({name: 'leavingDestinationListModification', content: 'OK'});
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
	selector: 'jhi-leaving-destination-popup',
	template: ''
})
export class LeavingDestinationPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private leavingDestinationPopupService: LeavingDestinationPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.leavingDestinationPopupService
					.open(LeavingDestinationDialogComponent, params['id']);
			} else {
				this.modalRef = this.leavingDestinationPopupService
					.open(LeavingDestinationDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
