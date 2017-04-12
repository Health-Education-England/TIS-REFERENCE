import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Nationality} from "./nationality.model";
import {NationalityPopupService} from "./nationality-popup.service";
import {NationalityService} from "./nationality.service";
@Component({
	selector: 'jhi-nationality-dialog',
	templateUrl: './nationality-dialog.component.html'
})
export class NationalityDialogComponent implements OnInit {

	nationality: Nationality;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private nationalityService: NationalityService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['nationality']);
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
		if (this.nationality.id !== undefined) {
			this.nationalityService.update(this.nationality)
				.subscribe((res: Nationality) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.nationalityService.create(this.nationality)
				.subscribe((res: Nationality) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Nationality) {
		this.eventManager.broadcast({name: 'nationalityListModification', content: 'OK'});
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
	selector: 'jhi-nationality-popup',
	template: ''
})
export class NationalityPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private nationalityPopupService: NationalityPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.nationalityPopupService
					.open(NationalityDialogComponent, params['id']);
			} else {
				this.modalRef = this.nationalityPopupService
					.open(NationalityDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
