import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {TrustDetailComponent} from "../../../../../../main/webapp/app/entities/trust/trust-detail.component";
import {TrustService} from "../../../../../../main/webapp/app/entities/trust/trust.service";
import {Trust} from "../../../../../../main/webapp/app/entities/trust/trust.model";

describe('Component Tests', () => {

	describe('Trust Management Detail Component', () => {
		let comp: TrustDetailComponent;
		let fixture: ComponentFixture<TrustDetailComponent>;
		let service: TrustService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [TrustDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					TrustService
				]
			}).overrideComponent(TrustDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(TrustDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(TrustService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Trust(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.trust).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
