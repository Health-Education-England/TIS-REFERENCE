import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Settled} from "./settled.model";
import {SettledPopupService} from "./settled-popup.service";
import {SettledService} from "./settled.service";
@Component({
	selector: 'jhi-settled-dialog',
	templateUrl: './settled-dialog.component.html'
})
export class SettledDialogComponent implements OnInit {

	settled: Settled;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private settledService: SettledService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['settled']);
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
		if (this.settled.id !== undefined) {
			this.settledService.update(this.settled)
				.subscribe((res: Settled) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.settledService.create(this.settled)
				.subscribe((res: Settled) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Settled) {
		this.eventManager.broadcast({name: 'settledListModification', content: 'OK'});
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
	selector: 'jhi-settled-popup',
	template: ''
})
export class SettledPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private settledPopupService: SettledPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.settledPopupService
					.open(SettledDialogComponent, params['id']);
			} else {
				this.modalRef = this.settledPopupService
					.open(SettledDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
