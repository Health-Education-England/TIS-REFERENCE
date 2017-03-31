import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {ReligiousBelief} from "./religious-belief.model";
import {ReligiousBeliefPopupService} from "./religious-belief-popup.service";
import {ReligiousBeliefService} from "./religious-belief.service";
@Component({
	selector: 'jhi-religious-belief-dialog',
	templateUrl: './religious-belief-dialog.component.html'
})
export class ReligiousBeliefDialogComponent implements OnInit {

	religiousBelief: ReligiousBelief;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private religiousBeliefService: ReligiousBeliefService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['religiousBelief']);
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
		if (this.religiousBelief.id !== undefined) {
			this.religiousBeliefService.update(this.religiousBelief)
				.subscribe((res: ReligiousBelief) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.religiousBeliefService.create(this.religiousBelief)
				.subscribe((res: ReligiousBelief) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: ReligiousBelief) {
		this.eventManager.broadcast({name: 'religiousBeliefListModification', content: 'OK'});
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
	selector: 'jhi-religious-belief-popup',
	template: ''
})
export class ReligiousBeliefPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private religiousBeliefPopupService: ReligiousBeliefPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.religiousBeliefPopupService
					.open(ReligiousBeliefDialogComponent, params['id']);
			} else {
				this.modalRef = this.religiousBeliefPopupService
					.open(ReligiousBeliefDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
