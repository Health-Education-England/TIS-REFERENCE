import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {ProgrammeMembershipType} from "./programme-membership-type.model";
import {ProgrammeMembershipTypePopupService} from "./programme-membership-type-popup.service";
import {ProgrammeMembershipTypeService} from "./programme-membership-type.service";
@Component({
	selector: 'jhi-programme-membership-type-dialog',
	templateUrl: './programme-membership-type-dialog.component.html'
})
export class ProgrammeMembershipTypeDialogComponent implements OnInit {

	programmeMembershipType: ProgrammeMembershipType;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private programmeMembershipTypeService: ProgrammeMembershipTypeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['programmeMembershipType']);
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
		if (this.programmeMembershipType.id !== undefined) {
			this.programmeMembershipTypeService.update(this.programmeMembershipType)
				.subscribe((res: ProgrammeMembershipType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.programmeMembershipTypeService.create(this.programmeMembershipType)
				.subscribe((res: ProgrammeMembershipType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: ProgrammeMembershipType) {
		this.eventManager.broadcast({name: 'programmeMembershipTypeListModification', content: 'OK'});
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
	selector: 'jhi-programme-membership-type-popup',
	template: ''
})
export class ProgrammeMembershipTypePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private programmeMembershipTypePopupService: ProgrammeMembershipTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.programmeMembershipTypePopupService
					.open(ProgrammeMembershipTypeDialogComponent, params['id']);
			} else {
				this.modalRef = this.programmeMembershipTypePopupService
					.open(ProgrammeMembershipTypeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
