import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {SexualOrientation} from "./sexual-orientation.model";
import {SexualOrientationPopupService} from "./sexual-orientation-popup.service";
import {SexualOrientationService} from "./sexual-orientation.service";
@Component({
	selector: 'jhi-sexual-orientation-dialog',
	templateUrl: './sexual-orientation-dialog.component.html'
})
export class SexualOrientationDialogComponent implements OnInit {

	sexualOrientation: SexualOrientation;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private sexualOrientationService: SexualOrientationService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['sexualOrientation']);
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
		if (this.sexualOrientation.id !== undefined) {
			this.sexualOrientationService.update(this.sexualOrientation)
				.subscribe((res: SexualOrientation) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.sexualOrientationService.create(this.sexualOrientation)
				.subscribe((res: SexualOrientation) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: SexualOrientation) {
		this.eventManager.broadcast({name: 'sexualOrientationListModification', content: 'OK'});
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
	selector: 'jhi-sexual-orientation-popup',
	template: ''
})
export class SexualOrientationPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private sexualOrientationPopupService: SexualOrientationPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.sexualOrientationPopupService
					.open(SexualOrientationDialogComponent, params['id']);
			} else {
				this.modalRef = this.sexualOrientationPopupService
					.open(SexualOrientationDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
